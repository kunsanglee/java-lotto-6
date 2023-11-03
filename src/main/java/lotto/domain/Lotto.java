package lotto.domain;

import static lotto.domain.LottoNumberRules.EXACT_LOTTO_COUNTS;
import static lotto.exception.ExceptionMessage.BONUS_NUMBER_ALREADY_CONTAINS_IN_WINNING_NUMBERS;
import static lotto.exception.ExceptionMessage.LOTTO_COUNTS_INVALID;
import static lotto.exception.ExceptionMessage.LOTTO_NUMBERS_DUPLICATED;

import java.util.List;
import java.util.stream.Collectors;
import lotto.exception.LottoGameException;

public class Lotto {
    private final List<LottoNumber> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = convertToLottoNumbers(numbers);
    }

    private void validate(List<Integer> numbers) {
        if (isInvalidSize(numbers)) {
            throw new LottoGameException(String.format(LOTTO_COUNTS_INVALID.getMessage(), numbers.size()));
        }
        if (isDuplicated(numbers)) {
            throw new LottoGameException(String.format(LOTTO_NUMBERS_DUPLICATED.getMessage(), numbers));
        }
    }

    private static boolean isInvalidSize(List<Integer> numbers) {
        return numbers.size() != EXACT_LOTTO_COUNTS.getValue();
    }

    private static boolean isDuplicated(List<Integer> numbers) {
        return numbers.stream().distinct().count() != EXACT_LOTTO_COUNTS.getValue();
    }

    private static List<LottoNumber> convertToLottoNumbers(List<Integer> numbers) {
        return numbers.stream()
                .map(LottoNumber::new)
                .collect(Collectors.toList());
    }

    public List<LottoNumber> getNumbers() {
        return this.numbers.stream()
                .map(lottoNumber -> new LottoNumber(lottoNumber.getNumber()))
                .collect(Collectors.toUnmodifiableList());
    }

    public void contains(int bonusNumber) {
        this.numbers.stream()
                .map(LottoNumber::getNumber)
                .filter(number -> number == bonusNumber)
                .findAny()
                .orElseThrow(() -> new LottoGameException(
                        String.format(BONUS_NUMBER_ALREADY_CONTAINS_IN_WINNING_NUMBERS.getMessage(), this.numbers,
                                bonusNumber)));
    }
}
