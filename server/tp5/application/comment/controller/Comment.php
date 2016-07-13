<?php
namespace app\comment\controller;

class Comment
{
    public function getComment(){
        $comment = new \app\comment\model\Comment();
        return json_encode($comment -> getComment());
    }

    public function setComment(){
        $comment = new \app\comment\model\Comment();
        return json_encode($comment -> setComment());
    }
}
