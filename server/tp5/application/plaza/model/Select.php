<?php
namespace app\plaza\model;

class Select extends \think\Model
{

  public function art(){
    if(\app\user\model\User::check()==1){

      $row=\think\Db::query("select A_id,A_title,A_picture from article");
      $row1=\think\Db::query("select A_id,A_title,A_picture from article where A_type = 1");
      $row2=\think\Db::query("select A_id,A_title,A_picture from article where A_type = 2");
      $row3=\think\Db::query("select A_id,A_title,A_picture from article where A_type = 3");
      $row4=\think\Db::query("select A_id,A_title,A_picture from article where A_type = 4");
      $row5=\think\Db::query("select A_id,A_title,A_picture from article where A_type = 5");
      $list=[$row,$row1,$row2,$row3,$row4,$row5];
      $data = ['list' => $list];
      $code = '1';
      $msg = 'OK';
    }else{
      $data = '';
      $code = '0';
      $msg = 'wrong user_id and token';
    }
    return ['data' => $data, 'code' => $code, 'msg' => $msg];
  }
}

