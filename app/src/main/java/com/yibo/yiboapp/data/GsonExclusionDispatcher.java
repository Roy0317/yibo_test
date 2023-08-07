package com.yibo.yiboapp.data;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangy on 2016/5/11.
 * exclude some fields or clazzs which don wanna be converted by Gson plugin
 * it mean those fileds or classes will not be convert into json string or object.
 */
public class GsonExclusionDispatcher implements ExclusionStrategy{

    public final String TAG = GsonExclusionDispatcher.this.getClass().getSimpleName();

    String[] excludeFields;
    Class<?>[] excludeClasses;

    public GsonExclusionDispatcher(String[] excludeFields,
                                Class<?>[] excludeClasses) {
        this.excludeFields = excludeFields;
        this.excludeClasses = excludeClasses;
    }

    public GsonExclusionDispatcher(String[] excludeFields) {
        this.excludeFields = excludeFields;
    }

    public GsonExclusionDispatcher(Class<?>[] excludeClasses) {
        this.excludeClasses = excludeClasses;
    }

    /**
     * filter the special fields which is user dont wanna exlude.
     * @param execludeClass
     * @param fields
     */
    private String[] isFieldsExisted(Class<?> execludeClass,String[] fields){
        List<String> fieldList = new ArrayList<String>();
        for (String field : fields){
            if (!Utils.isEmptyString(field)){
                if (existsField(execludeClass,field)){
                    fieldList.add(field);
                }
            }
        }
        if (!fieldList.isEmpty()){
            return (String[])fieldList.toArray();
        }
        return null;
    }

    /**
     * whether there exit the special field in class or sper class
     * @param clz
     * @param fieldName
     * @return
     */
    public static boolean existsField(Class clz,String fieldName){
        try {
            return clz.getDeclaredField(fieldName)!=null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if(clz!=Object.class){
            return existsField(clz.getSuperclass(),fieldName);
        }
        return false;
    }


    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {

        if (this.excludeFields == null)return false;
        for (String field : this.excludeFields){
            if (field.equals(fieldAttributes.getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        if (this.excludeClasses == null)return false;
        for (Class<?> c : this.excludeClasses){
            if (c.getName().equals(aClass.getName())){
                return true;
            }
        }
        return false;
    }

}
