<?php
namespace app\plaza\controller;

use app\plaza\model\Select;

class Plaza
{
   function art(){
     $test = new \app\plaza\model\Select();
     $plaza = $test -> art();
     return json_encode($plaza);
   }
}

