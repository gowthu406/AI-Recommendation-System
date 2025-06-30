package com.codtech;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.util.List;

public class RecommenderEngine {
    private Recommender recommender;

    private String dataFilePath;

    public RecommenderEngine(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getRecommendations(int userId, int howMany) throws Exception {
        DataModel model = new FileDataModel(new File(dataFilePath));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        List<RecommendedItem> recommendations = recommender.recommend(userId, howMany);
        if (recommendations.isEmpty()) {
            return "No recommendations available for user " + userId;
        }

        StringBuilder sb = new StringBuilder();
        for (RecommendedItem item : recommendations) {
            sb.append("Item ID: ").append(item.getItemID())
              .append(" | Score: ").append(String.format("%.2f", item.getValue()))
              .append("\n");
        }
        return sb.toString();
    }
}