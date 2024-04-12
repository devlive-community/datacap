package io.edurt.datacap.server.controller;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.BaseEntity;
import io.edurt.datacap.service.repository.BaseRepository;
import io.edurt.datacap.service.service.BaseService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

public abstract class BaseController<T extends BaseEntity>
        implements Serializable
{
    private final BaseRepository repository;
    private final BaseService<T> service;

    protected BaseController(BaseRepository repository, BaseService<T> service)
    {
        this.repository = repository;
        this.service = service;
    }

    /**
     * Get data based on pagination
     */
    @PostMapping(value = "list")
    public CommonResponse list(@RequestBody FilterBody filter)
    {
        return service.getAll(repository, filter);
    }

    /**
     * Save changes
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public CommonResponse saveAndUpdate(@RequestBody T configure)
    {
        return service.saveOrUpdate(repository, configure);
    }

    @Deprecated
    @DeleteMapping
    public CommonResponse delete(@RequestParam(value = "id") Long id)
    {
        return service.deleteById(repository, id);
    }

    @DeleteMapping(value = "{id}")
    public CommonResponse deleteForPath(@PathVariable(value = "id") Long id)
    {
        return service.deleteById(repository, id);
    }

    /**
     * Retrieves information for a specific path.
     *
     * @param id the identifier of the path
     * @return the information for the specified path
     */
    @GetMapping(value = "{id}")
    public CommonResponse<T> getInfoForPath(@PathVariable(value = "id") Long id)
    {
        return service.getById(repository, id);
    }

    @GetMapping(value = "info/{code}")
    public CommonResponse<T> getByCode(@PathVariable(value = "code") String code)
    {
        return service.findByCode(repository, code);
    }
}
