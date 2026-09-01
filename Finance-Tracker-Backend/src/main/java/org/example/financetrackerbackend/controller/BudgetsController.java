package org.example.financetrackerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.financetrackerbackend.dto.BudgetsDto;
import org.example.financetrackerbackend.dto.PeriodType;
import org.example.financetrackerbackend.entity.Budgets;
import org.example.financetrackerbackend.service.BudgetsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budgets")
public class BudgetsController {
    private final BudgetsService budgetsService;

    @PostMapping("/add-budgets/{username}")
    public ResponseEntity<BudgetsDto.BudgetsResponse> addBudgets(@RequestBody BudgetsDto.BudgetsRequest request,
                                                                 @PathVariable String username){
        BudgetsDto.BudgetsResponse responseString = budgetsService.addBudgets(request, username );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseString);
    }

    @GetMapping("/get-budgets/{username}")
    public ResponseEntity<List<BudgetsDto.BudgetsResponse>> getBudgets(@PathVariable String username){
        List<BudgetsDto.BudgetsResponse> responseString = budgetsService.findBudgetsByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

    @DeleteMapping("/delete-budgets/{username}/{id}")
    public ResponseEntity<String> deleteBudgets(@PathVariable Long id ,@PathVariable String username){
        budgetsService.deleteBudgets(id, username);
        return ResponseEntity.status(HttpStatus.OK).body("Budgets deleted successfully!");
    }

    @PutMapping("/update-budgets/{id}")
    public ResponseEntity<BudgetsDto.BudgetsResponse> updateBudgets(@RequestBody BudgetsDto.BudgetsRequest request,
                                                                    @PathVariable Long id){
        BudgetsDto.BudgetsResponse responseString = budgetsService.updateBudgets(request, String.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

    @GetMapping("/get-budgets-by-period-type/{username}/{periodType}")
    public ResponseEntity<List<BudgetsDto.BudgetsResponse>> getBudgetsByPeriodType( @PathVariable String username,@PathVariable PeriodType periodType){
        List<BudgetsDto.BudgetsResponse> responseString = budgetsService.getBudgetsByPeriodType(periodType, username);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }

    @GetMapping("/get-budgets-by-category/{username}/{category}")
    public ResponseEntity<List<BudgetsDto.BudgetsResponse>> getBudgetsByCategory(@PathVariable String category , @PathVariable String username){
        List<BudgetsDto.BudgetsResponse> responseString = budgetsService.getBudgetsByCategory(category, username);
        return ResponseEntity.status(HttpStatus.OK).body(responseString);
    }
}
