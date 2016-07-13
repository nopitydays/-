<?php
namespace app\share\controller;

class Share
{
    public function getShare(){
        $share = new \app\share\model\Share();
        return json_encode($share -> getShare());
    }

    public function setShare(){
        $share = new \app\share\model\Share();
        return json_encode($share -> setShare());
    }
}
