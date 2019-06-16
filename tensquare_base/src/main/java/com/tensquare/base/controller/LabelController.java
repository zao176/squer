package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ZAO
 * @Date: 2019/6/4 11:27
 * @Description:
 */

@RestController
@RequestMapping("/label")
@CrossOrigin//跨域
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 查询全部列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET )
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findAll());
    }


    /**
     * 根据id查询标签
     * @param labelid
     * @return
     */
    @RequestMapping(value="/{labelid}", method = RequestMethod.GET)
    public Result findById(@PathVariable String labelid){
        System.out.println("No.1");
        return new Result(true,StatusCode.OK,"查询成功",labelService.findById(labelid)  );
    }
    /**
     * 增加标签
     * @param label
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Label label){
        labelService.add(label);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     *  修改标签
     * @param label
     * @param labelid
     * @return
     */
    @RequestMapping(value="/{labelid}",method = RequestMethod.PUT)
    public  Result update(@RequestBody Label label,@PathVariable String labelid){
        label.setId(labelid);
        labelService.update(label);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除标签
     * @param labelid
     */
    @RequestMapping(value = "/{labelid}",method =RequestMethod.DELETE )
    public Result  deleteById(@PathVariable String labelid){
        labelService.deleteById(labelid);
        return new Result(true,StatusCode.OK,"删除成功");
    }


    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/{search}")
    public Result findSearch(@RequestBody Map searchMap){
        List<Label> labels = (List<Label>) labelService.findSearch(searchMap);
        return   Result.success("查询成功",labels);
    }

    /**
     * 分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/{search}/{page}/{size}")
    public Result findSearch(@RequestBody Map searchMap,@PathVariable int page,@PathVariable int size){
       Page<Label> labelpage=labelService.findSearch(searchMap,page,size);
        PageResult<Label> pageResult = new PageResult<>(labelpage.getTotalElements(), labelpage.getContent());
        return   Result.success("查询成功",pageResult);
    }

}
