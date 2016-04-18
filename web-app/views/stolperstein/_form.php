<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;

/* @var $this yii\web\View */
/* @var $model app\models\Stolperstein */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="stolperstein-form">

    <?php $form = ActiveForm::begin(); ?>
    
    <?= $form->field($model, 'name')->textInput(['maxlength' => 2000000000]) ?>

    <?= $form->field($model, 'adresse')->textInput(['maxlength' => 2000000000]) ?>

    <?= $form->field($model, 'longitude')->textInput(['maxlength' => 2000000000]) ?>

    <?= $form->field($model, 'latitude')->textInput(['maxlength' => 2000000000]) ?>
    
    <?= $form->field($model, 'verlegedatum')->textInput(['maxlength' => 2000000000]) ?>

    <?= $form->field($model, 'geboren')->textInput(['maxlength' => 2000000000]) ?>

    <?= $form->field($model, 'tod')->textInput(['maxlength' => 2000000000]) ?>

    <?= $form->field($model, 'image_id')->textInput() ?>

    <?= $form->field($model, 'bio_id')->textInput() ?>

    <div class="form-group">
        <?= Html::submitButton($model->isNewRecord ? 'Create' : 'Update', ['class' => $model->isNewRecord ? 'btn btn-success' : 'btn btn-primary']) ?>
    </div>

    <?php ActiveForm::end(); ?>

</div>
