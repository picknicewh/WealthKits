package com.cfjn.javacf.modle;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

public class TypeEntity {
	
	@NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
    @Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    @Column(column = "typecode")
	private int typeCode;
	
	public int getTypeCode() {
		return typeCode;
	}
	
	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	@Column(column = "count")
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
