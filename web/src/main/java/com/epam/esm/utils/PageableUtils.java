package com.epam.esm.utils;

import com.epam.esm.exceptions.WrongParameterException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.constants.InitValues.BASE_SORT;
import static com.epam.esm.exceptions.ExceptionCodesConstants.WRONG_PARAMETER;

@UtilityClass
@Slf4j
public class PageableUtils {
    private static final String DELIMITER = ":";
    private static final int SORTING_TYPE_INDEX = 1;
    private static final int SORTING_VALUE_INDEX = 0;

    public static Object createPageableWithSorting(int page, int size, String[] sortBy) throws WrongParameterException {
//        List<Sort.Order> sortOrders = new ArrayList<>();
//        if (sortBy != null) {
//            try {
//                sortOrders = Arrays.stream(sortBy).map(
//                        s -> new Sort.Order(Sort.Direction.fromString(s.split(DELIMITER)[SORTING_TYPE_INDEX]),
//                                s.split(DELIMITER)[SORTING_VALUE_INDEX]))
//                        .collect(Collectors.toList());
//            } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
//                log.warn("Illegal sort param = {}", sortBy, e);
//                throw new WrongParameterException("Wrong parameter in sorting value", WRONG_PARAMETER);
//            }
//        }
//        return PageRequest.of(page, size, Sort.by(sortOrders));
    return null;
    }

    public static Object createPageableWithSorting(int page, int size) throws WrongParameterException {
        return createPageableWithSorting(page, size, new String[]{BASE_SORT});
    }
}
