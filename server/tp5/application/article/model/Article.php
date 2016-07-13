<?php
namespace app\Article\model;

use think\Model;
class Article extends Model{

    public function today(){
        if(\app\user\model\User::check() == 1){
            $list = \think\Db::query('SELECT A_id, A_title, A_picture FROM recommendation JOIN article ON A_id = R_article_id ORDER BY R_dayart DESC LIMIT 0,10');
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

    public function plaza(){
        if(\app\user\model\User::check() == 1){
            if($_POST['type']==0)
                $data = \think\Db::query('SELECT A_id, A_title, A_picture FROM article ORDER BY A_upvote_num DESC, A_dayart DESC');
            else if($_POST['type']==1)
                $data = \think\Db::query('SELECT A_id, A_title, A_picture FROM article WHERE A_type=1 ORDER BY A_upvote_num DESC, A_dayart DESC');
            else if($_POST['type']==2)
                $data = \think\Db::query('SELECT A_id, A_title, A_picture FROM article WHERE A_type=2 ORDER BY A_upvote_num DESC, A_dayart DESC');
            else if($_POST['type']==3)
                $data = \think\Db::query('SELECT A_id, A_title, A_picture FROM article WHERE A_type=3 ORDER BY A_upvote_num DESC, A_dayart DESC');
            else if($_POST['type']==4)
                $data = \think\Db::query('SELECT A_id, A_title, A_picture FROM article WHERE A_type=4 ORDER BY A_upvote_num DESC, A_dayart DESC');
            else if($_POST['type']==5)
                $data = \think\Db::query('SELECT A_id, A_title, A_picture FROM article WHERE A_type=5 ORDER BY A_upvote_num DESC, A_dayart DESC');
            $code = '1';
            $msg = 'OK';
        } else {
            $data = '';
            $code = '0';
            $msg = 'wrong user_id and token';
        }

        return ['data' => $data, 'code' => $code, 'msg' => $msg];
    }

    public function article(){
        if(\app\user\model\User::check() == 1){
            $d = \think\Db::query('SELECT A_title, A_type, A_picture, A_content, A_upvote_num, A_author, A_dayart FROM article WHERE A_id=:article_id',
                                  ['article_id' => $_POST['article_id']]);
            $data = $d[0];
            $code = '1';
            $msg = 'OK';
        } else {
            $data = '';
            $code = '0';
            $msg = 'wrong user_id and token';
        }

        return ['data' => $data, 'code' => $code, 'msg' => $msg];
    }
}
