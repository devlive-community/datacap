package io.edurt.datacap.spi.adapter;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import io.edurt.datacap.file.FileFilter;
import io.edurt.datacap.file.model.FileRequest;
import io.edurt.datacap.file.model.FileResponse;
import io.edurt.datacap.spi.model.Response;

import java.util.List;

public interface Adapter
{
    Response handlerExecute(String content);

    default List<Object> handlerFormatter(Injector injector, String format, List<String> headers, List<Object> columns)
    {
        return FileFilter.findNotify(injector, format)
                .map(file -> {
                    FileRequest request = new FileRequest();
                    request.setHeaders(headers);
                    request.setColumns(columns);

                    FileResponse response = file.format(request);
                    Preconditions.checkArgument(Boolean.TRUE.equals(response.getSuccessful()), "Not support format: %s", format);
                    return response.getColumns();
                })
                .orElseThrow(() -> new IllegalArgumentException("Unsupported format: " + format));
    }
}
