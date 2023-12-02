package io.edurt.datacap.fs;

public interface Fs
{
    default String name()
    {
        return this.getClass()
                .getSimpleName()
                .replace("FileSystem", "");
    }

    default String description()
    {
        return String.format("Integrate %s file system", this.name());
    }

    FsResponse writer(FsRequest request);

    FsResponse reader(FsRequest request);
}
