<?php
namespace app\Comment\model;

use think\Model;
class Comment extends Model{

    public function getComment(){
        if(\app\user\model\User::check() == 1){
            $list = \think\Db::query('SELECT CM_id, CM_user_id, name, avatar, CM_content, CM_dayart, CM_upvote_num FROM comment JOIN user ON CM_user_id=id WHERE CM_article_id=:article_id ORDER BY CM_upvote_num DESC, CM_dayart DESC',
                                  ['article_id' => $_POST['article_id']]);
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

    public function setComment(){
        if(\app\user\model\User::check() == 1){
             \think\Db::execute('INSERT INTO comment (CM_user_id, CM_article_id, CM_content) VALUES (:user_id, :article_id, :content)',
                               ['user_id'	=> $_POST['user_id'],
                                'article_id'	=> $_POST['article_id'],
                                'content'	=> $_POST['content'],
                               ]);
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
