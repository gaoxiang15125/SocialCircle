package github.zlg.socialcircle.module.define;

import lombok.Getter;

/**
 * @program: socialcircle
 * @description: 数据库数据相关枚举值
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-14 11:05
 **/
public class DatabaseEnum {
    // 之后修改为从配置文件加载
    public static final String DEFAULT_DESCRIPTION = "这个人很懒，什么都没有留下";

    @Getter
    public enum SEX_TYPE {
        boy(1, "男"), girl(2, "女"), secret(0, "保密");

        int code;
        String sexStr;

        SEX_TYPE(int type, String sexStr) {
            this.code = type;
            this.sexStr = sexStr;
        }

        public static SEX_TYPE getSexByCode(int code) {
            for (SEX_TYPE sexType : SEX_TYPE.values()) {
                if (sexType.getCode() == code) {
                    return sexType;
                }
            }
            return secret;
        }

        public static SEX_TYPE getSexByStr(String sexStr) {
            for (SEX_TYPE sexType : SEX_TYPE.values()) {
                if (sexType.getSexStr().equals(sexStr)) {
                    return sexType;
                }
            }
            return secret;
        }
    }

    @Getter
    public enum MEMBER_TYPE {

        member(0, "成员"), QManager(1, "管理员"), QLeader(2, "圈主");

        String memberStr;
        int code;

        MEMBER_TYPE(int code, String memberStr) {
            this.code = code;
            this.memberStr = memberStr;
        }

        public static MEMBER_TYPE getTypeByCode(int code) {
            for (MEMBER_TYPE memberType : MEMBER_TYPE.values()) {
                if (memberType.getCode() == code) {
                    return memberType;
                }
            }
            return member;
        }

        public static MEMBER_TYPE getTypeByStr(String memberStr) {
            for (MEMBER_TYPE memberType : MEMBER_TYPE.values()) {
                if (memberType.getMemberStr().equals(memberStr)) {
                    return memberType;
                }
            }
            return member;
        }
    }

    @Getter
    public enum ORDER_TYPE {
        /**
         * 数据库查询排序方式，默认情况下采用倒叙排列，因为人们期望看到最新的动态
         */
        DESC(0),ASC(1);

        ORDER_TYPE(int code) {
            this.code = code;
        }

        int code;

        public static ORDER_TYPE getTypeByCode(int code) {
            for (ORDER_TYPE memberType : ORDER_TYPE.values()) {
                if (memberType.getCode() == code) {
                    return memberType;
                }
            }
            return DESC;
        }
    }
}
