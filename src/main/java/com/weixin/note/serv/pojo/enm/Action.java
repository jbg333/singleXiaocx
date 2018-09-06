package com.weixin.note.serv.pojo.enm;

public enum Action {
	 /** 正常（默认） */
    Normal("0", "正常执行"),

    /** 跳过 */
    Skip("1", "跳过相应操作");

    /** 主键 */
    private final String key;

    /** 描述 */
    private final String desc;

    Action(final String key, final String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }
}
