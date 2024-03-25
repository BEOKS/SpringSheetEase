package com.beoks.springwebexcel;

import jakarta.servlet.http.HttpServletResponse;
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
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class ExcelDownloadAspect {

    @Around("@annotation(ExcelDownload)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행 전 로직
        // 예를 들어, HttpServletResponse 객체를 찾기
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

        // 실제 메소드 실행
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
                    }
                    fs.writeFilesystem(response.getOutputStream());
                }
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
}
