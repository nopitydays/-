<?php
namespace app\upvote\model;

use think\Model;
class Upvote extends Model{

    public function upvoteComment(){
        if(\app\user\model\User::check() == 1){
            $r = \think\Db::query('SELECT CU_id FROM comment_upvote WHERE CU_user_id=:user_id AND CU_comment_id=:comment_id', 
                              ['user_id'    => $_POST['user_id'],
                               'comment_id' => $_POST['comment_id']]);
            if($r){
                $data = '';
                $code = '3';
                $msg = 'upvote already';
            } else {
                \think\Db::execute('INSERT INTO comment_upvote (CU_user_id, CU_comment_id) VALUES (:user_id, :comment_id)',
                              ['user_id'    => $_POST['user_id'],
                               'comment_id' => $_POST['comment_id']]);
                $data = '';
                $code = '2';
                $msg = 'OK';
            }
        } else {
            $data = '';
            $code = '0';
            $msg = 'wrong user_id and token';
        }

        return ['data' => $data, 'code' => $code, 'msg' => $msg];
    }

    public function upvoteArticle(){
        if(\app\user\model\User::check() == 1){
            $r = \think\Db::query('SELECT U_id FROM upvote WHERE U_user_id=:user_id AND U_article_id=:article_id', 
                              ['user_id'    => $_POST['user_id'],
                               'article_id' => $_POST['article_id']]);
            if($r){
                $data = '';
                $code = '3';
                $msg = 'upvote already';
            } else {
                \think\Db::execute('INSERT INTO upvote (U_user_id, U_article_id) VALUES (:user_id, :article_id)',
                              ['user_id'    => $_POST['user_id'],
                               'article_id' => $_POST['article_id']]);
                $data = '';
                $code = '2';
                $msg = 'OK';
            }
        } else {
            $data = '';
            $code = '0';
            $msg = 'wrong user_id and token';
        }

        return ['data' => $data, 'code' => $code, 'msg' => $msg];
    }
}
