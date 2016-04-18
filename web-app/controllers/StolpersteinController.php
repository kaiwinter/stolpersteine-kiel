<?php

namespace app\controllers;

use Yii;
use app\models\Stolperstein;
use app\models\StolpersteinSearch;
use yii\web\Controller;
use yii\web\NotFoundHttpException;
use yii\filters\VerbFilter;

/**
 * StolpersteinController implements the CRUD actions for Stolperstein model.
 */
class StolpersteinController extends Controller
{
    public function behaviors()
    {
        return [
            'verbs' => [
                'class' => VerbFilter::className(),
                'actions' => [
                    'delete' => ['post'],
                ],
            ],
        ];
    }

    /**
     * Lists all Stolperstein models.
     * @return mixed
     */
    public function actionIndex()
    {
        $searchModel = new StolpersteinSearch();
        $dataProvider = $searchModel->search(Yii::$app->request->queryParams);

        return $this->render('index', [
            'searchModel' => $searchModel,
            'dataProvider' => $dataProvider,
        ]);
    }

    /**
     * Displays a single Stolperstein model.
     * @param integer $id
     * @return mixed
     */
    public function actionView($id)
    {
        return $this->render('view', [
            'model' => $this->findModel($id),
        ]);
    }

    /**
     * Creates a new Stolperstein model.
     * If creation is successful, the browser will be redirected to the 'view' page.
     * @return mixed
     */
    public function actionCreate()
    {
        $model = new Stolperstein();

        if ($model->load(Yii::$app->request->post()) && $model->save()) {
            return $this->redirect(['view', 'id' => $model->id]);
        } else {
            return $this->render('create', [
                'model' => $model,
            ]);
        }
    }

    /**
     * Updates an existing Stolperstein model.
     * If update is successful, the browser will be redirected to the 'view' page.
     * @param integer $id
     * @return mixed
     */
    public function actionUpdate($id)
    {
        $model = $this->findModel($id);

        if ($model->load(Yii::$app->request->post()) && $model->save()) {
            return $this->redirect(['view', 'id' => $model->id]);
        } else {
            return $this->render('update', [
                'model' => $model,
            	 'title' => 'Update',
            ]);
        }
    }

    /**
     * Deletes an existing Stolperstein model.
     * If deletion is successful, the browser will be redirected to the 'index' page.
     * @param integer $id
     * @return mixed
     */
    public function actionDelete($id)
    {
        $this->findModel($id)->delete();

        return $this->redirect(['index']);
    }

    /**
     * Finds the Stolperstein model based on its primary key value.
     * If the model is not found, a 404 HTTP exception will be thrown.
     * @param integer $id
     * @return Stolperstein the loaded model
     * @throws NotFoundHttpException if the model cannot be found
     */
    protected function findModel($id)
    {
        if (($model = Stolperstein::findOne($id)) !== null) {
            return $model;
        } else {
            throw new NotFoundHttpException('The requested page does not exist.');
        }
    }
    
    public function actionClone($id)
    {
    	$model_original = $this->findModel($id);
    	$model = new Stolperstein();
    	
    	$model->adresse = $model_original->adresse;
    	$model->verlegedatum = $model_original->verlegedatum;
    	$model->name = $model_original->name;
    	$model->geboren = $model_original->geboren;
    	$model->tod = $model_original->tod;
    	$model->longitude = $model_original->longitude;
    	$model->latitude = $model_original->latitude;
    	$model->image_id = $model_original->image_id;
    	$model->bio_id = $model_original->bio_id;
    	
    	if ($model->load(Yii::$app->request->post()) && $model->save()) {
    		return $this->redirect(['view', 'id' => $model->id]);
    	} else {
    		return $this->render('update', [
    				'model' => $model,
    				'title' => 'Clone',
    				]);
    	}		
    }
}
