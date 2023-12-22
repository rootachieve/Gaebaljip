package com.gaebaljip.exceed.achieve.application;

import com.gaebaljip.exceed.achieve.application.port.in.GetAchieveUsecase;
import com.gaebaljip.exceed.achieve.domain.Achieve;
import com.gaebaljip.exceed.dto.GetAchieve;
import com.gaebaljip.exceed.dto.GetAchieveListResponse;
import com.gaebaljip.exceed.food.domain.FoodModel;
import com.gaebaljip.exceed.meal.application.port.out.LoadMealPort;
import com.gaebaljip.exceed.meal.domain.MealModel;
import com.gaebaljip.exceed.meal.domain.MealType;
import com.gaebaljip.exceed.member.domain.Activity;
import com.gaebaljip.exceed.member.domain.MemberModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAchieveService implements GetAchieveUsecase {

    private final LoadMealPort loadMealPort;
    @Override
    public GetAchieveListResponse execute(Long memberId, LocalDate date) {

        MealModel query = loadMealPort.query(memberId, date);
        MemberModel memberModel = MemberModel.builder()
                .gender(1)
                .age(25)
                .activity(Activity.NOT_ACTIVE)
                .height(171.2)
                .weight(60.2)
                .build();

        FoodModel foodModel = FoodModel.builder()
                .name("밥")
                .calorie(400)
                .carbohydrate(300)
                .protein(800)
                .fat(20)
                .build();

        FoodModel foodModel1 = FoodModel.builder()
                .name("된장찌개")
                .calorie(1000)
                .carbohydrate(600)
                .protein(300)
                .fat(100)
                .build();

        MealModel mealModel = MealModel.builder()
                .mealType(MealType.LUNCH)
                .foodModels(List.of(foodModel, foodModel1))
                .build();

        Achieve achieve = Achieve.builder()
                .build();

        GetAchieve getAchieve = new GetAchieve(true,
                LocalDate.now(),
                achieve.calculateCalorieAchieveRate(mealModel.getCurentCalorie(), memberModel.measureTargetCalorie()),
                achieve.evaluateCarbohydrateAchieve(mealModel.getCurrentCarbohydrate(), memberModel.measureTargetCarbohydrate()),
                achieve.evaluateProteinAchieve(mealModel.getCurrentProtein(), memberModel.measureTargetProtein()),
                achieve.evaluateFatAchieve(mealModel.getCurrentFat(), memberModel.measureTargetFat()));

        return new GetAchieveListResponse(List.of(getAchieve));
    }
}
