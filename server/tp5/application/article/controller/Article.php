<?php
namespace app\article\controller;

class Article
{
    public function today(){
        $article = new \app\article\model\Article();
        return json_encode($article -> today());
    }

    public function plaza(){
        $article = new \app\article\model\Article();
        return json_encode($article -> plaza());
    }

    public function article(){
        $article = new \app\article\model\Article();
        return json_encode($article -> article());
    }
}
