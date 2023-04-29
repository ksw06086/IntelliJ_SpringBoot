package com.godcoder.myhome.validator;

import com.godcoder.myhome.model.Board;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) { // 어떤 클래스를 Validator 지원 할 것인지? 정해줌
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) { // 값이 유효한지 확인해주는 메소드
        Board b = (Board) target;
        if(StringUtils.isEmpty(b.getContent())){ // 내용이 비었을 경우
            errors.rejectValue("content", "key", "내용을 입력하세요.");
        }
    }
}
