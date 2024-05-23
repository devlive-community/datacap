package io.edurt.datacap.common.utils;

public class UrlUtils
{
    private UrlUtils()
    {
    }

    /**
     * Replaces multiple consecutive slashes in the given URL with a single slash.
     *
     * @param url the URL to fix
     * @return the fixed URL with single slashes
     */
    public static String fixUrl(String url)
    {
        return url.replaceAll("/+", "/");
    }
}
