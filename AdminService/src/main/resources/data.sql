insert into admin_account (user_id, user_password, role_types, nickname, email, memo, created_at, created_by,
                          modified_at, modified_by)
values ('chan1', '{noop}123', 'ADMIN', 'chan', 'chan1@mail.com', 'I am chan.', now(), 'chan', now(), 'chan')
;
insert into admin_account (user_id, user_password, role_types, nickname, email, memo, created_at, created_by,
                          modified_at, modified_by)
values ('chan2', '{noop}123', 'MANAGER', 'chan', 'chan2@mail.com', 'I am chan.', now(), 'chan', now(), 'chan')
;
insert into admin_account (user_id, user_password, role_types, nickname, email, memo, created_at, created_by,
                          modified_at, modified_by)
values ('chan3', '{noop}123', 'DEVELOPER', 'chan', 'chan3@mail.com', 'I am chan.', now(), 'chan', now(), 'chan')
;
insert into admin_account (user_id, user_password, role_types, nickname, email, memo, created_at, created_by,
                          modified_at, modified_by)
values ('chan4', '{noop}123', 'USER', 'chan', 'chan4@mail.com', 'I am chan.', now(), 'chan', now(), 'chan')
;