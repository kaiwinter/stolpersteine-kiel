<?php

use yii\helpers\Html;


/* @var $this yii\web\View */
/* @var $model app\models\Stolperstein */

$this->title = 'Create Stolperstein';
$this->params['breadcrumbs'][] = ['label' => 'Stolpersteins', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="stolperstein-create">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
