package com.beoks.springwebexcel;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

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

        // @ExcelDownload 어노테이션에서 파일 이름 가져오기
        ExcelDownload excelDownload = method.getAnnotation(ExcelDownload.class);
        String fileName = excelDownload.fileName();

        // 메소드 실행 후 로직 (엑셀 파일 생성 및 응답 반환)
        XSSFExcelFileConverterBuilder builder = new XSSFExcelFileConverterBuilder();
        try (XSSFWorkbook workbook = builder.fileName(fileName).data(result).build()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            workbook.write(response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            // 오류 처리
        }

        // 메소드 실행 결과 반환
        return result;
    }
}
