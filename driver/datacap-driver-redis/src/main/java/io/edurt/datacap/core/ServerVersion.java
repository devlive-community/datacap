package io.edurt.datacap.core;

import lombok.Data;

@Data
public class ServerVersion
{
    private String origin;

    private Integer major;
    private Integer minor;
    private Integer patch;

    public ServerVersion(String origin)
    {
        this.origin = origin;

        String[] arr = origin.split("\\.");
        for (int i = 0; i < arr.length; i++) {
            String str = arr[i];
            int v = Utils.isNumber(str) ? Integer.parseInt(str) : 0;
            if (i == 0) {
                this.major = v;
            }
            else if (i == 1) {
                minor = v;
            }
            else if (i == 2) {
                patch = v;
            }
        }
    }
}
