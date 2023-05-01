insert into article (id, title, content, created_by, modified_by, created_at, modified_at, hashtag)
values (1,
        'Quisque ut erat.',
        'Vestibulum quam sapien, varius ut, blandit non, interdum in, ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae Duis faucibus accumsan odio. Curabitur convallis. Duis consequat dui nec nisi volutpat eleifend. Donec ut dolor. Morbi vel lectus in quam fringilla rhoncus.
Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis. Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero.',
        '#pink',
        'Kamilah',
        '2021-05-30 23:53:46',
        '2021-03-10 08:48:50',
        '#test1'),
        (2, 'Morbi ut odio.', 'Phasellus in felis. Donec semper sapien a libero. Nam dui.
Proin leo odio, porttitor id, consequat in, consequat ut, nulla. Sed accumsan felis. Ut at dolor quis odio consequat varius.
Integer ac leo. Pellentesque ultrices mattis odio. Donec vitae nisi.
#purple', 'Arv', 'Keelby', '2021-05-06 11:51:24', '2021-05-23 08:34:54', '#test2');

insert into comment (id, article_id, content, created_at, modified_at, created_by, modified_by)
values (1, 1, '퍼가요~', '2021-03-02 22:40:04', '2021-04-27 15:38:09', 'user1', 'user1'),
       (2, 1, '퍼가요~', '2021-03-02 22:40:05', '2021-04-27 15:38:10', 'user2', 'user2');

