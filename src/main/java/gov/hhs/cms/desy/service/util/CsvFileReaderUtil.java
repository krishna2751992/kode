package gov.hhs.cms.desy.service.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.ResourceUtils;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import gov.hhs.cms.desy.service.dto.RequestStatusDTO;

public class CsvFileReaderUtil {

	private CsvFileReaderUtil() {
	}

	public static List<RequestStatusDTO> getRequestStatus() throws IOException {
		List<RequestStatusDTO> listOfRequestStatusDTO = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(ResourceUtils.getFile("classpath:requestStatus.csv").toPath())) {
			ColumnPositionMappingStrategy<RequestStatusDTO> strategy = new ColumnPositionMappingStrategy<RequestStatusDTO>();
			strategy.setType(RequestStatusDTO.class);
			String[] memberFieldsToBindTo = { "duaNumber", "requestId", "requestedOn", "status", "statusDetails" };
			strategy.setColumnMapping(memberFieldsToBindTo);

			CsvToBean<RequestStatusDTO> csvToBean = new CsvToBeanBuilder<RequestStatusDTO>(reader)
					.withMappingStrategy(strategy).withSkipLines(1).withIgnoreLeadingWhiteSpace(true).build();

			Iterator<RequestStatusDTO> requestStatusIterator = csvToBean.iterator();

			while (requestStatusIterator.hasNext()) {
				RequestStatusDTO requestStatus = requestStatusIterator.next();
				listOfRequestStatusDTO.add(requestStatus);
			}

		}
		return listOfRequestStatusDTO;

	}
}
