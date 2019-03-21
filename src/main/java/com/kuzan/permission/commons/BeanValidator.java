package com.kuzan.permission.commons;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * Created by sui on 2019/3/21.
 */
public class BeanValidator {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 检验单个对象
     * @param t
     * @param groups
     * @param <T>
     * @return
     */
    private static <T> Map<String,String> validate(T t,Class... groups){
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        if (violationSet.isEmpty()){
            return Collections.emptyMap();
        }else {
            LinkedHashMap<String,String> errors = Maps.newLinkedHashMap();
            Iterator iterator = violationSet.iterator();
            while (iterator.hasNext()){
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(),violation.getMessage());
            }
            return errors;
        }
    }

    /**
     * 校验传入的对象数组
     * @param collection
     * @return
     */
    private static Map<String,String> validate(Collection<?> collection){
        Preconditions.checkNotNull(collection);
        if (CollectionUtils.isNotEmpty(collection)){
            Iterator iterator = collection.iterator();
            Map errors;
            do {
                if (!iterator.hasNext()){
                    return Collections.emptyMap();
                }
                errors = validate(iterator.next(),new Class[0]);
            }while (errors.isEmpty());
            return errors;
        }
        return Collections.emptyMap();
    }

    /**
     * public 方法
     * @param first
     * @param objs
     * @return
     */
    public static Map<String,String> validate(Object first,Object... objs){
        if (CollectionUtils.isNotEmpty(Arrays.asList(objs))){
            return validate(Lists.asList(first,objs));
        }else {
            return validate(first,new Class[0]);
        }
    }
}
