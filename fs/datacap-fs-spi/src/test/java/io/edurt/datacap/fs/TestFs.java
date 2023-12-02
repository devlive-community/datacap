package io.edurt.datacap.fs;

public class TestFs
        implements Fs
{
    @Override
    public FsResponse writer(FsRequest request)
    {
        return FsResponse.builder()
                .build();
    }

    @Override
    public FsResponse reader(FsRequest request)
    {
        return FsResponse.builder()
                .build();
    }
}
