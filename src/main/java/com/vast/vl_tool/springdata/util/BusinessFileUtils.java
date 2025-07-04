package com.vast.vl_tool.springdata.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author vastlan
 * @description
 * @created 2024/8/9 10:36
 */
public class BusinessFileUtils {

//  public static void main(String[] args) throws Exception {
//      createBusinessFiles(
//      "D:\\my-workspace\\wukong\\server\\eagle-cloud-plus\\project\\src\\main\\java\\com\\cnqisoft\\project\\entity\\management",
//      "D:\\my-workspace\\wukong\\server\\eagle-cloud-plus\\project\\src\\main\\java\\com\\cnqisoft\\project\\repository",
//      "D:\\my-workspace\\wukong\\server\\eagle-cloud-plus\\project\\src\\main\\java\\com\\cnqisoft\\project\\service",
//      null,
//      null,
//      "com.cnqisoft.project"
//    );
//  }

  public static void createBusinessFiles() {
    String[] ignoreRepositoryFile = {"MotorCadeDetail"};
    String[] ignoreServiceFile = {"MotorCadeDetail", "OrderDetail", "SaleTypeDictionary", "TransportTypeDictionary"};

    createBusinessFiles(
      "G:\\workspace\\server\\enterprise\\shs\\eagle-cloud-plus\\dispatch\\src\\main\\java\\com\\cnqisoft\\dispatch\\entity",
      "G:\\workspace\\server\\enterprise\\shs\\eagle-cloud-plus\\dispatch\\src\\main\\java\\com\\cnqisoft\\dispatch\\repository",
      "G:\\workspace\\server\\enterprise\\shs\\eagle-cloud-plus\\dispatch\\src\\main\\java\\com\\cnqisoft\\dispatch\\service",
      ignoreRepositoryFile,
      ignoreServiceFile,
      "com.cnqisoft.dispatch"
    );
  }

  public static void createBusinessFiles(String entityFolderPath, String repositoryFolderPath, String serviceFolderPath, String[] ignoreRepositoryFile, String[] ignoreServiceFile, String packagePath) {
    // source
    Path entityPath = Path.of(entityFolderPath);
    File entityPathFile = entityPath.toFile();
    String[] entityChildList = entityPathFile.list();

    // repository
    Path repPath = Path.of(repositoryFolderPath);
    File repPathFile = repPath.toFile();
    String[] repChildList = repPathFile.list();

    // service
    Path serePath = Path.of(serviceFolderPath);
    File serPathFile = serePath.toFile();
    String[] serChildList = serPathFile.list();

    for (String entityFile : entityChildList) {
      String entityPrefixName = entityFile.replace(".java", "");

      if (ignoreRepositoryFile != null && ignoreRepositoryFile.length > 0) {
        String matchIgnore = Arrays.stream(ignoreRepositoryFile).filter(item -> entityPrefixName.equals(item)).findAny().orElse(null);

        if (matchIgnore != null) {
          continue;
        }
      }

      String repFileNamePrefix = entityPrefixName + "Repository";
      String repFileName = repFileNamePrefix + ".java";
      String matchRepFile = Arrays.stream(repChildList).filter(item -> repFileName.equals(item)).findAny().orElse(null);

      String serFileNamePrefix = entityPrefixName + "Service";
      String serFileName = serFileNamePrefix + ".java";
      String matchSerFile = Arrays.stream(serChildList).filter(item -> serFileName.equals(item)).findAny().orElse(null);

      try {
        Class idType = getIdType(entityFolderPath + "\\" + entityFile);

        String entitySubstringVal = entityFolderPath.substring(entityFolderPath.lastIndexOf("entity"), entityFolderPath.length());
        String[] entitySubstringValArr = entitySubstringVal.split("\\\\");
        String entityPackagePath = "entity";

        if (entitySubstringValArr.length > 1) {
          entityPackagePath = "";

          for (int i = 0, size = entitySubstringValArr.length; i < size; i++) {
            if (i != 0) {
              entityPackagePath += ".";
            }

            entityPackagePath += entitySubstringValArr[i];
          }
        }

        if (matchRepFile == null) {
          createRepositoryFile((repositoryFolderPath + "\\" + repFileName), entityPrefixName, repFileNamePrefix, idType, packagePath, entityPackagePath);
        }

        if (matchSerFile == null) {
          createServiceFile(serviceFolderPath, serFileName, entityPrefixName, serFileNamePrefix, idType, packagePath, entityPackagePath);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static void createRepositoryFile(String filePath, String entityName, String className, Class idType, String packagePath, String entityPackagePath) throws Exception {
    boolean isIdTypeNull = idType == null;

    if (isIdTypeNull) {
      System.err.println(entityName + "非法id类型");
    }

    File repFile = new File(filePath);

    if (repFile.exists()) {
      System.out.println(filePath + "已存在");
      return;
    }

    repFile.createNewFile();
    System.out.println("创建文件" + filePath + ", id类型为" + (isIdTypeNull ? "null" : idType.getName()));

    LocalDateTime now = LocalDateTime.now();

    String idTypeStr = isIdTypeNull ? "null" : idType.getName().replace((idType.getPackageName() + "."), "");

    String content = "package " + packagePath + ".repository;\n" +
      "\n" +
      "import " + packagePath + "." + entityPackagePath + "." + entityName + ";\n" +
      "import org.springframework.data.jpa.repository.JpaRepository;\n" +
      "import org.springframework.stereotype.Repository;\n" +
      "\n" +
      "import " + (isIdTypeNull ? "null" : idType.getName()) + ";\n" +
      "\n" +
      "/**\n" +
      " * @author vastlan\n" +
      " * @description\n" +
      " * @created " + now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth() + " " + now.getHour() +":" + now.getMinute() + "\n" +
      " */\n" +
      "\n" +
      "@Repository\n" +
      "public interface " + className + " extends JpaRepository<" + entityName + ", " + idTypeStr + "> {\n" +
      "}\n";

    OutputStream outputStream = Files.newOutputStream(repFile.toPath());
    outputStream.write(content.getBytes(Charset.defaultCharset()));

    outputStream.close();
  }

  private static void createServiceFile(String relativePath, String fileName, String entityName, String className, Class idType, String packagePath, String entityPackagePath) throws Exception {
    boolean isIdTypeNull = idType == null;

    if (isIdTypeNull) {
      System.err.println(entityName + "非法id类型");
    }

    String filePath = relativePath + "\\" + fileName;

    File serFile = new File(filePath);

    if (serFile.exists()) {
      System.out.println(filePath + "已存在");
      return;
    }

    serFile.createNewFile();
    System.out.println("创建文件" + filePath + ", id类型为" + (isIdTypeNull ? "null" : idType.getName()));

    LocalDateTime now = LocalDateTime.now();

    String idTypeStr = isIdTypeNull ? "null" : idType.getName().replace((idType.getPackageName() + "."), "");

    String content = "package " + packagePath + ".service;\n" +
      "\n" +
      "import " + packagePath + "." + entityPackagePath + "." + entityName + ";\n" +
      "import com.cnqisoft.service.service.EntityService;\n" +
      "\n" +
      "import " + (isIdTypeNull ? "null" : idType.getName()) + ";\n" +
      "\n" +
      "/**\n" +
      " * @author vastlan\n" +
      " * @description\n" +
      " * @created " + now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth() + " " + now.getHour() +":" + now.getMinute() + "\n" +
      " */\n" +
      "public interface " + className + " extends EntityService<" + entityName + ", " + idTypeStr + "> {\n" +
      "}\n";

    OutputStream outputStream = Files.newOutputStream(serFile.toPath());
    outputStream.write(content.getBytes(Charset.defaultCharset()));

    outputStream.close();

    // impl
    String implRelativePath = relativePath + "\\impl";
    String implClassName = className + "Impl";
    String implFileName = implClassName + ".java";
    String implFilePath = implRelativePath + "\\" + implFileName;

    File serImplFile = new File(implFilePath);

    if (serImplFile.exists()) {
      System.out.println(implFilePath + "已存在");
      return;
    }

    serImplFile.createNewFile();
    System.out.println("创建文件" + implFilePath + ", id类型为" + (isIdTypeNull ? "null" : idType.getName()));

    String repositoryClassName = entityName + "Repository";

    String implContent = "package " + packagePath + ".service.impl;\n" +
      "\n" +
      "import " + packagePath + "." + entityPackagePath + "." + entityName + ";\n" +
      "import " + packagePath + ".repository." + repositoryClassName + ";\n" +
      "import " + packagePath + ".service." + className + ";\n" +
      "import com.cnqisoft.service.service.AbstractEntityService;\n" +
      "import org.springframework.stereotype.Service;\n" +
      "\n" +
      "import " + (isIdTypeNull ? "null" : idType.getName()) + ";\n" +
      "\n" +
      "/**\n" +
      " * @author vastlan\n" +
      " * @description\n" +
      " * @created " + now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth() + " " + now.getHour() +":" + now.getMinute() + "\n" +
      " */\n" +
      "\n" +
      "@Service\n" +
      "public class " + implClassName + " extends AbstractEntityService<" + entityName + ", " + idTypeStr + ", " + repositoryClassName + "> \n" +
      "  implements " + className + " {\n" +
      "  \n" +
      "  public " + implClassName + "(" + repositoryClassName + " repository) {\n" +
      "    super(repository);\n" +
      "  }\n" +
      "}\n";

    OutputStream implOutputStream = Files.newOutputStream(serImplFile.toPath());
    implOutputStream.write(implContent.getBytes(Charset.defaultCharset()));

    implOutputStream.close();
  }

  private static Class getIdType(String filePath) throws Exception {
    File repFile = new File(filePath);

    BufferedReader bufferedReader = Files.newBufferedReader(repFile.toPath());

    String line;

    while ((line = bufferedReader.readLine()) != null) {
      if (line.contains("extends IncrementBusinessBase")) {
        bufferedReader.close();
        return Integer.class;
      } else if (line.contains("extends BusinessBase")) {
        bufferedReader.close();
        return UUID.class;
      }
    }

    return null;
  }
}
