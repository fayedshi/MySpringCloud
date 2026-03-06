CREATE TABLE `cloud_user` (
    `id`           BIGINT AUTO_INCREMENT COMMENT '主键ID',
    `username`     VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`     VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    `account_non_expired`   TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '账号是否过期 (1-过期, 0-未过期)',
    `account_non_locked`    TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '账号是否锁定 (1-锁定, 0-未锁定)',
    `credentials_non_expired` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '凭证是否过期 (1-过期, 0-未过期)',
    `is_enabled`   TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '是否启用 (1-启用, 0-禁用)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;



CREATE  TABLE roles (
 id  INT  NOT  NULL  AUTO_INCREMENT,
  NAME  VARCHAR(50)  NOT  NULL,
  DESCRIPTION  VARCHAR(255),
  PRIMARY  KEY (id)
);

-- 创建权限表
CREATE TABLE permissions (
  id INT NOT null AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
   PRIMARY KEY (id)
);

-- 创建角色-权限关联表
CREATE TABLE role_permissions (
  id INT NOT null AUTO_INCREMENT,
  role_id INT,
  permission_id INT,
     PRIMARY KEY (id)
);

-- 创建用户-角色关联表
CREATE TABLE user_roles (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT,
  role_id INT,
  PRIMARY KEY (id)
);

-- 插入角色数据
INSERT INTO roles (id, name, description) VALUES
(1, 'ADMIN', 'fulle control'),
(2, 'USER', 'edit content'),
(3, 'GUEST', 'browse content');

-- 插入资源数据
INSERT INTO resources (id, name, description) VALUES
(1, '文章', '存储网站的文章内容');

-- 插入权限数据
INSERT INTO permissions (id, name, description) VALUES
(1, 'Modify', 'create, modify or delete'),
(2, 'List', 'list objects');

-- 插入用户数据
INSERT INTO users (id, name) VALUES
(1, '张一'),
(2, '张二'),
(3, '张三');

-- 插入角色-权限关联数据
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 2),
(3, 1);

-- 插入用户-角色关联数据
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 2),
(2, 1),
(3, 2);