<?php
namespace app\upvote\controller;

class Upvote
{

    public function upvoteComment(){
        $upvote = new \app\upvote\model\Upvote();
        return json_encode($upvote -> upvoteComment());
    }

    public function upvoteArticle(){
        $upvote = new \app\upvote\model\Upvote();
        return json_encode($upvote -> upvoteArticle());
    }
}
