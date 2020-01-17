package sqlsession;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLColumn {
    String columnName();
    Class columnType() default Object.class;
    String keyType() default "NULL";
}
