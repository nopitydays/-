<?php
namespace app\user\model;

use think\Model;
class User extends Model{

    public function login(){
        $r = \think\Db::query("select * from user where name='".$_POST['username']."'");
        if($r){
            if($_POST['password'] === $r[0]['password']){
                $token = User::getRandChar(30);
                \think\Db::execute('insert into token (T_user_id, T_token) values (:id, :token)', ['id' => $r[0]['id'], 'token' => $token]);
                $data = ['token'	=> $token,
                         'id'		=> $r[0]['id'],
                         'avatar'	=> $r[0]['avatar'],
                         'name'		=> $r[0]['name']];
                $code = '1';
                $msg = 'OK';
            } else {
                $data = '';
                $code = '0';
                $msg = 'wrong password';
            }
        } else {
            $data = '';
            $code = '0';
            $msg = 'no such user';
        }
        
        return ['data' => $data, 'code' => $code, 'msg' => $msg];
    }

    public function logout(){
        if(User::check() == 1){
            \think\Db::execute('delete from token where T_user_id=:user_id and T_token=:token',
                                   ['user_id' => $_POST['user_id'],
                                    'token'   => $_POST['token']]);
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

    public function sign(){
        $r = \think\Db::query("select id from user where name='".$_POST['username']."'");
        if($r){
            $data = '';
            $code = '0';
            $msg = 'user exists';
        } else {
            if(filter_var($_POST['email'], FILTER_VALIDATE_EMAIL)){
                \think\Db::execute('insert into user (name, email, password) values (:name, :email, :password)',
                                  ['name'		=> $_POST['username'],
                                   'email'		=> $_POST['email'],
                                   'password'	=> $_POST['password']]
                                  );
                $data = '';
                $code = '2';
                $msg = 'OK';
            } else {
                $data = '';
                $code = '0';
                $msg = 'Wrong email address format';
            }
        }

        return ['data' => $data, 'code' => $code, 'msg' => $msg];
    }

    public function setProfile(){
        if(User::check() == 1){
            \think\Db::execute('UPDATE user SET avatar=:head_id, sex=:sex, birthday=:birthday, area=:area, signature=:signature, blog=:blog, qq=:qq, nickname=:nickname where id=:user_id',
                               ['head_id'	=> $_POST['head_id'],
                                'sex'		=> $_POST['sex'],
                                'birthday'	=> $_POST['birthday'],
                                'area'		=> $_POST['area'],
                                'signature'	=> $_POST['signature'],
                                'blog'		=> $_POST['blog'],
                                'qq'		=> $_POST['qq'],
                                'nickname'	=> $_POST['nickname'],
                                'user_id'	=> $_POST['user_id'],
                               ]
                              );
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

    public function getProfile(){
        if(User::check() == 1){
            $id = $_POST['view_id'];
            $d = \think\Db::query('select * from user where id='.$id);
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

    public function getFriend(){
        if(\app\user\model\User::check() == 1){
            $list = \think\Db::query('select avatar, id, name from friend JOIN user ON F_user_id_2=id where F_user_id_1=:user_id group by id',
                                   ['user_id' => $_POST['user_id']]);
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

    public function setFriend(){
        if(\app\user\model\User::check() == 1){
            $id1 = $_POST['user_id'];
            $id2 = $_POST['friend_id'];
            $r = \think\Db::execute('SELECT F_id FROM friend WHERE F_user_id_1=:id1 AND F_user_id_2=:id2', ['id1' => $id1, 'id2' => $id2]);
            if($r){
                $data = '';
                $code = '3';
                $msg = 'friend already';
            } else {
                \think\Db::execute('INSERT INTO friend (F_user_id_1, F_user_id_2) VALUE ('.$id1.', '.$id2.')');
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

    static function check(){
        $r = \think\Db::query('select T_token from token where T_user_id=:user_id and T_token=:token',
                              ['user_id' => $_POST['user_id'],
                               'token'   => $_POST['token']]);
        if ($r){
            return 1;
        } else {
            return 0;
        }
    }

    function getRandChar($length){
        $str = null;
        $strPol = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        $max = strlen($strPol) - 1;

        for($i=0; $i < $length; $i++){
        $str .= $strPol[rand(0, $max)];
        }

        return $str;
  }
}
