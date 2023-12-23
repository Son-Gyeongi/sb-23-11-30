package com.ll.sb231130.global.rsData;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RsData<T> {
    private final String resultCode;
    private final String msg;
    private final T data;
    private final int statusCode;

    // RsData<T> 객체 만들어준다.
    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        // 문자열 resultCode를 숫자 statusCode로 변환
        int statusCode = Integer.parseInt(resultCode);

        return new RsData<>(resultCode, msg, data, statusCode);
    }

    public static RsData<?> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 400;
    }

    public boolean isFail() {
        return !isSuccess();
    }

    //  "of"라는 이름의 제네릭 메서드로, T 타입의 데이터를 받아들이고 RsData<T> 타입의 객체를 반환한다는 의미
    public <T> RsData<T> of(T data) {
        // 데이터만 새로 입력받고 나머지는 그대로 가져다 쓴다.
        return RsData.of(resultCode, msg, data);
    }
}
