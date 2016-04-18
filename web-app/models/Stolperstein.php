<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "stolperstein".
 *
 * @property integer $id
 * @property string $adresse
 * @property string $verlegedatum
 * @property string $name
 * @property string $geboren
 * @property string $tod
 * @property string $longitude
 * @property string $latitude
 * @property integer $image_id
 * @property integer $bio_id
 */
class Stolperstein extends \yii\db\ActiveRecord
{
    /**
     * @inheritdoc
     */
    public static function tableName()
    {
        return 'stolperstein';
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['image_id', 'bio_id'], 'integer'],
            [['adresse', 'verlegedatum', 'name', 'geboren', 'tod', 'longitude', 'latitude'], 'string', 'max' => 2000000000]
        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return [
            'id' => 'ID',
            'adresse' => 'Adresse',
            'verlegedatum' => 'Verlegedatum',
            'name' => 'Name',
            'geboren' => 'Geboren',
            'tod' => 'Tod',
            'longitude' => 'Longitude',
            'latitude' => 'Latitude',
            'image_id' => 'Image ID',
            'bio_id' => 'Bio ID',
        ];
    }
}
