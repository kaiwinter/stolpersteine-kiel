<?php

namespace app\models;

use Yii;
use yii\base\Model;
use yii\data\ActiveDataProvider;
use app\models\Stolperstein;

/**
 * StolpersteinSearch represents the model behind the search form about `app\models\Stolperstein`.
 */
class StolpersteinSearch extends Stolperstein
{
    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['id', 'image_id', 'bio_id'], 'integer'],
            [['adresse', 'verlegedatum', 'name', 'geboren', 'tod', 'longitude', 'latitude'], 'safe'],
        ];
    }

    /**
     * @inheritdoc
     */
    public function scenarios()
    {
        // bypass scenarios() implementation in the parent class
        return Model::scenarios();
    }

    /**
     * Creates data provider instance with search query applied
     *
     * @param array $params
     *
     * @return ActiveDataProvider
     */
    public function search($params)
    {
        $query = Stolperstein::find();

        $dataProvider = new ActiveDataProvider([
            'query' => $query,
        ]);

        if (!($this->load($params) && $this->validate())) {
            return $dataProvider;
        }

        $query->andFilterWhere([
            'id' => $this->id,
            'image_id' => $this->image_id,
            'bio_id' => $this->bio_id,
        ]);

        $query->andFilterWhere(['like', 'adresse', $this->adresse])
            ->andFilterWhere(['like', 'verlegedatum', $this->verlegedatum])
            ->andFilterWhere(['like', 'name', $this->name])
            ->andFilterWhere(['like', 'geboren', $this->geboren])
            ->andFilterWhere(['like', 'tod', $this->tod])
            ->andFilterWhere(['like', 'longitude', $this->longitude])
            ->andFilterWhere(['like', 'latitude', $this->latitude]);

        return $dataProvider;
    }
}
