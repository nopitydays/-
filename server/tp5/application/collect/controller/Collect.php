<?php
namespace app\collect\controller;

class Collect
{
    public function getCollect(){
        $collect = new \app\collect\model\Collect();
        return json_encode($collect -> getCollect());
    }

    public function collectArticle(){
        $collect = new \app\collect\model\Collect();
        return json_encode($collect -> collectArticle());
    }
}
