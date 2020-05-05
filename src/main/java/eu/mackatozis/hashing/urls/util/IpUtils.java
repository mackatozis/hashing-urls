package eu.mackatozis.hashing.urls.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpUtils {

    private static final Pattern DECIMAL_PATTERN =
            Pattern.compile(
                    "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))");

    private static final Pattern OCTAL_PATTERN =
            Pattern.compile("([0-7]{1,4})\\.([0-7]{1,4})\\.([0-7]{1,4})\\.([0-7]{1,4})");

    private static final Pattern HEX_PATTERN =
            Pattern.compile(
                    "0x([0-9a-f]{1,2})\\.0x([0-9a-f]{1,2})\\.0x([0-9a-f]{1,2})\\.0x([0-9a-f]{1,2})",
                    Pattern.CASE_INSENSITIVE);

    public static boolean isIpAddress(String ip) {
        return isDecimalIpAddress(ip)
                || isOctalEncodedIpAddress(ip)
                || isHexEncodedIpAddress(ip)
                || isIntegerIpAddress(ip);
    }

    public static boolean isDecimalIpAddress(String ip) {
        return DECIMAL_PATTERN.matcher(ip).matches();
    }

    public static boolean isOctalEncodedIpAddress(String ip) {
        return OCTAL_PATTERN.matcher(ip).matches();
    }

    public static boolean isHexEncodedIpAddress(String ip) {
        return HEX_PATTERN.matcher(ip).matches();
    }

    public static boolean isIntegerIpAddress(String ip) {
        try {
            Integer.parseUnsignedInt(ip);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Converts the input IP address {@code String} into
     * a four-dot separated decimals, only when the input
     * is not in decimal format.
     *
     * @param ipAddress {@code String} to process
     * @return four-dot separated decimals {@code String};
     *         {@code null} otherwise
     */
    public static String resolveDecimalIpAddress(String ipAddress) {
        if (isDecimalIpAddress(ipAddress)) {
            return ipAddress;
        }

        Matcher matcherOct = OCTAL_PATTERN.matcher(ipAddress);
        if (matcherOct.matches()) {
            return convertToDecimal(matcherOct, 8);
        }

        Matcher matcherHex = HEX_PATTERN.matcher(ipAddress);
        if (matcherHex.matches()) {
            return convertToDecimal(matcherHex, 16);
        }

        if (isIntegerIpAddress(ipAddress)) {
            int ip = Integer.parseUnsignedInt(ipAddress);

            return String.format(
                    "%d.%d.%d.%d",
                    (ip >> 24 & 0xff), (ip >> 16 & 0xff), (ip >> 8 & 0xff), (ip & 0xff));
        }

        return null;
    }

    private static String convertToDecimal(Matcher matcher, int radix) {
        StringJoiner joiner = new StringJoiner(".");

        for (int i = 1; i <= 4; i++) {
            int decimal = Integer.parseInt(matcher.group(i), radix);
            joiner.add(String.valueOf(decimal));
        }

        return joiner.toString();
    }
}
