package apt;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

/**
 * 未运行成功，Java调用注解处理器太麻烦了，还是直接Android吧
 */
@SupportedAnnotationTypes("Hello")
public class HelloProcessor extends AbstractProcessor {
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        return super.getSupportedAnnotationTypes();
//    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello World!")
                .build();

        TypeSpec hello = TypeSpec.classBuilder("Hello")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile file = JavaFile.builder("apt", hello).build();

        try {
            file.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
