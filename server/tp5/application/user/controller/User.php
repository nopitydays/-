<?php
namespace app\user\controller;

class User
{
    public function login(){
        $user = new \app\user\model\User();
        return json_encode($user -> login());
    }

    public function logout(){
        $user = new \app\user\model\User();
        return json_encode($user -> logout());
    }

    public function sign(){
        $user = new \app\user\model\User();
        return json_encode($user -> sign());
    }

    public function setProfile(){
        $user = new \app\user\model\User();
        return json_encode($user -> setProfile());
    }

    public function getProfile(){
        $user = new \app\user\model\User();
        return json_encode($user -> getProfile());
    }

    public function setFriend(){
        $user = new \app\user\model\User();
        return json_encode($user -> setFriend());
    }

    public function getFriend(){
        $user = new \app\user\model\User();
        return json_encode($user -> getFriend());
    }
}
