package cc.ryanc.halo.service.impl;

import cc.ryanc.halo.model.domain.Category;
import cc.ryanc.halo.repository.CategoryRepository;
import cc.ryanc.halo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

/**
 * @author : RYAN0UP
 * @date : 2017/11/30
 * @version : 1.0
 * description: Category业务层实现
 */
@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String CATEGORY_KEY = "'category_key'";

    private static final String CATEGORY_CACHE_NAME = "cate_cache";

    /**
     * 保存/修改分类目录 清除缓存
     *
     * @param category 分类目录
     * @return ategory
     */
    @CacheEvict(value = CATEGORY_CACHE_NAME,key = CATEGORY_KEY)
    @Override
    public Category saveByCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * 根据编号移除分类目录 清除缓存
     *
     * @param cateId 分类目录编号
     * @return Category
     */
    @CacheEvict(value = CATEGORY_CACHE_NAME,key = CATEGORY_KEY)
    @Override
    public Category removeByCateId(Long cateId) {
        Optional<Category> category = this.findByCateId(cateId);
        categoryRepository.delete(category.get());
        return category.get();
    }

    /**
     * 查询所有分类目录 缓存
     *
     * @return list
     */
    @Cacheable(value = CATEGORY_CACHE_NAME,key = CATEGORY_KEY)
    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 根据编号查询分类目录 缓存
     *
     * @param cateId 分类编号
     * @return Category
     */
    @Cacheable(value = CATEGORY_CACHE_NAME,key = "#cateId+'cate'")
    @Override
    public Optional<Category> findByCateId(Long cateId) {
        return categoryRepository.findById(cateId);
    }

    /**
     * 根据分类目录路径查询，用于验证是否已经存在该路径
     *
     * @param cateUrl cateUrl
     * @return category
     */
    @Override
    public Category findByCateUrl(String cateUrl) {
        return categoryRepository.findCategoryByCateUrl(cateUrl);
    }


    @Override
    public List<Category> strListToCateList(List<String> strings) {
        if(null==strings){
            return null;
        }
        List<Category> categories = new ArrayList<>();
        Optional<Category> category = null;
        for(String str:strings){
            category = findByCateId(Long.parseLong(str));
            categories.add(category.get());
        }
        return categories;
    }
}
