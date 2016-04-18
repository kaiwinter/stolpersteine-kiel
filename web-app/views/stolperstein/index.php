<?php

use yii\helpers\Html;
use yii\grid\GridView;

/* @var $this yii\web\View */
/* @var $searchModel app\models\StolpersteinSearch */
/* @var $dataProvider yii\data\ActiveDataProvider */

$this->title = 'Stolpersteins';
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="stolperstein-index">

    <h1><?= Html::encode($this->title) ?></h1>
    <?php // echo $this->render('_search', ['model' => $searchModel]); ?>

    <p>
        <?= Html::a('Create Stolperstein', ['create'], ['class' => 'btn btn-success']) ?>
    </p>

    <?= GridView::widget([
        'dataProvider' => $dataProvider,
        'filterModel' => $searchModel,
        'columns' => [
            ['class' => 'yii\grid\SerialColumn'],

            'id',
            'adresse',
            'verlegedatum',
            'name',
            'geboren',
            'tod',
            'longitude',
            'latitude',
            'image_id',
            'bio_id',

            ['class' => 'yii\grid\ActionColumn'],
				[
					'attribute' => 'some_title',
					'format' => 'raw',
					'value' => function ($model) {                      
							return Html::a('Clone', ['clone', 'id' => $model->id], ['class' => 'btn btn-primary']);
					}
				],
        ],
    ]); ?>

</div>
