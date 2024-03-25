package com.beoks.springwebexcel;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.util.List;

@Aspect
@Component
public class ExcelDownloadAspect {

    @Around("@annotation(ExcelDownload)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletResponse response = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletResponse) {
                response = (HttpServletResponse) arg;
                break;
            }
        }

        if (response == null) {
            throw new IllegalArgumentException("HttpServletResponse 파라미터가 필요합니다.");
        }

        List<?> result = (List<?>) joinPoint.proceed();
        System.out.println("ExcelDownloadAspect exec");
        System.out.println("result = " + result);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ExcelDownload excelDownload = method.getAnnotation(ExcelDownload.class);
        String fileName = excelDownload.fileName();
        boolean useFlatten = excelDownload.useFlatten();
        String password = excelDownload.password();

        XSSFExcelFileConverterBuilder builder = new XSSFExcelFileConverterBuilder();
        try (XSSFWorkbook workbook = builder.fileName(fileName).data(result).useFlatten(useFlatten).build()) {
            response.setHeader("Content-Disposition","attachment; filename="+fileName);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            if(!password.isEmpty()){
                responseEncryptedWorkBook(password, workbook, response);
            }
            else{
                workbook.write(response.getOutputStream());
                response.flushBuffer();
            }
        } catch (Exception e) {
            throw new RuntimeException("error occurred when convert data to excel file",e);
        }
        return result;
    }

    private static void responseEncryptedWorkBook(String password, XSSFWorkbook workbook, HttpServletResponse response) throws IOException {
        try (POIFSFileSystem fs = new POIFSFileSystem()) {
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            // EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile, CipherAlgorithm.aes192, HashAlgorithm.sha384, -1, -1, null);
            Encryptor enc = info.getEncryptor();
            enc.confirmPassword(password);
            // Read in an existing OOXML file and write to encrypted output stream
            // don't forget to close the output stream otherwise the padding bytes aren't added
            ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
            workbook.write(fileOut);
            try (OPCPackage opc = OPCPackage.open(new ByteArrayInputStream(fileOut.toByteArray()));
                 OutputStream os = enc.getDataStream(fs)) {
                opc.save(os);
            } catch (GeneralSecurityException | InvalidFormatException e) {
                throw new RuntimeException(e);
            }
            fs.writeFilesystem(response.getOutputStream());
        }
    }
}
