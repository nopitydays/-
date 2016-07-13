<?php
namespace app\share\model;

use think\Model;
class Share extends Model{

    public function getShare(){
        if(\app\user\model\User::check() == 1){
            if($_POST['view_id'] == $_POST['user_id']){
                $id=$_POST['view_id'];
                $list = \think\Db::query('SELECT avatar, id, name, S_content, A_id, A_title, A_picture, S_dayart 
				                          FROM share 
										  LEFT JOIN article ON S_article_id=A_id 
										  JOIN user ON S_user_id=id 
										  WHERE id='.$id.' 
										        OR id IN 
												    (SELECT F_user_id_2 FROM friend WHERE F_user_id_1='.$id.') 
										  ORDER BY S_dayart DESC');
            } else {
                $list = \think\Db::query('SELECT avatar, id, name, S_content, A_id, A_title, A_picture, S_dayart 
				                          FROM share 
										  LEFT JOIN article ON S_article_id=A_id 
										  JOIN user ON S_user_id=id 
										  where id=:id 
										  ORDER BY S_dayart DESC', 
                                              ['id' => $_POST['view_id']]);
            }
            $data = ['list' => $list];
            $code = '1';
            $msg = 'OK';
        } else {
            $data = '';
            $code = '0';
            $msg = 'wrong user_id and token';
        }

        return ['data' => $data, 'code' => $code, 'msg' => $msg];
    }

    public function setShare(){
        if(\app\user\model\User::check() == 1){
            if($_POST['article_id'] == 0){
                \think\Db::execute('INSERT INTO share (S_user_id, S_content) VALUES (:user_id, :content)',
                               ['user_id'	=> $_POST['user_id'],
                                'content'	=> $_POST['content'],
                               ]);
            } else {
            \think\Db::execute('INSERT INTO share (S_user_id, S_article_id, S_content) VALUES (:user_id, :article_id, :content)',
                               ['user_id'	=> $_POST['user_id'],
                                'article_id'	=> $_POST['article_id'],
                                'content'	=> $_POST['content'],
                               ]);
            }
            $data = '';
            $code = '2';
            $msg = 'OK';
        } else {
            $data = '';
            $code = '0';
            $msg = 'wrong user_id and token';
        }

        return ['data' => $data, 'code' => $code, 'msg' => $msg];
    }
}
