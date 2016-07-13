<?php
namespace app\collect\model;

use think\Model;
class Collect extends Model{

    public function getCollect(){
        if(\app\user\model\User::check() == 1){
            $id = $_POST['view_id'];
            $list = \think\Db::query('SELECT A_id, A_title, A_picture from article where A_id in (SELECT CL_article_id FROM collect WHERE CL_user_id='.$id.')');
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

    public function collectArticle(){
        if(\app\user\model\User::check() == 1){
            $r = \think\Db::query('SELECT CL_id FROM collect WHERE CL_user_id=:user_id AND CL_article_id=:article_id',
                              ['user_id'    => $_POST['user_id'],
                               'article_id' => $_POST['article_id']]);
            if($r){
                $data = '';
                $code = '3';
                $msg = 'collect already';
            } else {
                \think\Db::execute('INSERT INTO collect (CL_user_id, CL_article_id) VALUES (:user_id, :article_id)', 
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
