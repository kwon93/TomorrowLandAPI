package com.aaa.api.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsers extends EntityPathBase<Users> {

    private static final long serialVersionUID = -1989590036L;

    public static final QUsers users = new QUsers("users");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final EnumPath<com.aaa.api.domain.enumType.Role> role = createEnum("role", com.aaa.api.domain.enumType.Role.class);

    public final EnumPath<com.aaa.api.domain.enumType.UserLevel> userLevel = createEnum("userLevel", com.aaa.api.domain.enumType.UserLevel.class);

    public QUsers(String variable) {
        super(Users.class, forVariable(variable));
    }

    public QUsers(Path<? extends Users> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUsers(PathMetadata metadata) {
        super(Users.class, metadata);
    }

}

