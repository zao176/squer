package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: ZAO
 * @Date: 2019/6/4 11:25
 * @Description:
 */
@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部标签
     * @return
     */
   public List<Label> findAll(){
       return labelDao.findAll();
   }

    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    public Label findById(String id){

        return  labelDao.findById(id).get();
    }

    /**
     * 增加标签
     * @param label
     */
    public void add(Label label){
    label.setId(idWorker.nextId()+"");//设置id
    labelDao.save(label);

    }

    /**
     * 修改标签
     * @param label
     */
     public  void update(Label label){
        labelDao.save(label);
    }

    /**
     * 删除标签
     * @param id
     */
    public  void  deleteById(String id){
         labelDao.deleteById(id);
    }

    /**
     * 根据标签查询
     * @param searchMap
     * @return
     */
    public List<Label> findSearch(Map searchMap) {
        Specification<Label> specification = getLabelSpecification(searchMap);

        return  labelDao.findAll(specification);
    }

    /**
     * 拼接查询条件
     * @param searchMap
     * @return
     */
    private Specification<Label> getLabelSpecification(Map searchMap) {
        return (Specification<Label>) (root, criteriaQuery, cb) -> {

                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.isEmpty(searchMap.get("labelname"))){
                    predicateList .add( cb.like(root.get("labelname").as(String.class),"%"+"labelname"+"%"));
                }
                if (!StringUtils.isEmpty(searchMap.get("state"))){
                 predicateList.add( cb.equal(root.get("state").as(String.class),"state"));
                }
                if (!StringUtils.isEmpty(searchMap.get("recommend"))){
                   predicateList.add( cb.equal(root.get("recommend").as(String.class),"recommend"));
                }
               //组装条件
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            };
    }

    /**
     * 分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findSearch(Map searchMap, int page, int size) {
        Specification<Label> specification = getLabelSpecification(searchMap);
        return  labelDao.findAll(specification, PageRequest.of(page-1,size));
    }
}
