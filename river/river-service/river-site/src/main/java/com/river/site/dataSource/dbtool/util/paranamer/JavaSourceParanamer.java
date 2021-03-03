package com.river.site.dataSource.dbtool.util.paranamer;

import com.river.site.dataSource.dbtool.util.IOHelper;
import com.river.site.dataSource.dbtool.util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSourceParanamer
  implements Paranamer
{
  private ClassLoader classLoader;

  public JavaSourceParanamer(ClassLoader classLoader)
  {
    if (classLoader == null) {
      throw new IllegalArgumentException("'classLoader' must be not null");
    }
    this.classLoader = classLoader;
  }

  @Override
  public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
    return lookupParameterNames(methodOrConstructor, true);
  }

  @Override
  public String[] lookupParameterNames(AccessibleObject methodOrConstructor, boolean throwExceptionIfMissing)
  {
    try {
      JavaSourceFileMethodParametersParser parser = new JavaSourceFileMethodParametersParser();
      String javaSource = null;
      if ((methodOrConstructor instanceof Method)) {
        Method m = (Method)methodOrConstructor;
        javaSource = m.getDeclaringClass().getName().replace('.', '/') + ".java";
      } else if ((methodOrConstructor instanceof Constructor)) {
        Constructor c = (Constructor)methodOrConstructor;
        javaSource = c.getDeclaringClass().getName().replace('.', '/') + ".java";
      } else {
        throw new IllegalArgumentException("unknow AccessibleObject" + methodOrConstructor + ",must be Method or Constructor");
      }

      InputStream javaSourceInputStream = this.classLoader.getResourceAsStream(javaSource);
      try
      {
        String[] arrayOfString;
        if (javaSourceInputStream != null) {
          return parser.parseJavaFileForParamNames(methodOrConstructor, IOHelper.toString(javaSourceInputStream));
        }
        return Paranamer.EMPTY_NAMES;
      } finally {
        if (javaSourceInputStream != null) {
          javaSourceInputStream.close();
        }
      }
    } catch (IOException e) {
      if (throwExceptionIfMissing) {
        throw new RuntimeException("IOException while reading javasource,method:" + methodOrConstructor, e);
      }
    }
    return Paranamer.EMPTY_NAMES;
  }

  public static class JavaSourceFileMethodParametersParser
  {
    public String[] parseJavaFileForParamNames(Constructor<?> constructor, String content)
    {
      return parseJavaFileForParamNames(content, constructor.getName(), constructor.getParameterTypes());
    }

    public String[] parseJavaFileForParamNames(Method method, String content) {
      return parseJavaFileForParamNames(content, method.getName(), method.getParameterTypes());
    }

    public String[] parseJavaFileForParamNames(AccessibleObject methodOrConstructor, String content) {
      if ((methodOrConstructor instanceof Method)) {
        return parseJavaFileForParamNames((Method)methodOrConstructor, content);
      }
      if ((methodOrConstructor instanceof Constructor)) {
        return parseJavaFileForParamNames((Constructor)methodOrConstructor, content);
      }
      throw new IllegalArgumentException("unknow AccessibleObject" + methodOrConstructor + ",must be Method or Constructor");
    }

    private String[] parseJavaFileForParamNames(String content, String name, Class<?>[] parameterTypes)
    {
      Pattern methodPattern = Pattern.compile("(?s)" + name + "\\s*\\(" + getParamsPattern(parameterTypes) + "\\)\\s*\\{");
      Matcher m = methodPattern.matcher(content);
      List paramNames = new ArrayList();
      if (m.find()) {
        for (int i = 1; i <= parameterTypes.length; i++) {
          paramNames.add(m.group(i));
        }
        return (String[])paramNames.toArray(new String[0]);
      }
      return null;
    }

    private String getParamsPattern(Class<?>[] parameterTypes) {
      List paramPatterns = new ArrayList();
      for (int i = 0; i < parameterTypes.length; i++) {
        Class type = parameterTypes[i];
        String paramPattern = ".*" + type.getSimpleName() + ".*\\s+(\\w+).*";
        paramPatterns.add(paramPattern);
      }
      return StringHelper.join(paramPatterns, ",");
    }
  }
}
