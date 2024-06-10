package com.sds.cmsapp.model.folder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds.cmsapp.domain.Document;
import com.sds.cmsapp.domain.Folder;
import com.sds.cmsapp.domain.Trash;
import com.sds.cmsapp.exception.FolderException;
import com.sds.cmsapp.model.document.DocumentDAO;
import com.sds.cmsapp.model.document.DocumentVersionDAO;
import com.sds.cmsapp.model.emp.EmpDAO;
import com.sds.cmsapp.model.trash.TrashDAO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FolderServiceImpl implements FolderService {
	
	@Autowired
	private FolderDAO folderDAO;
	
	@Autowired
	private DocumentDAO documentDAO;
	
	@Autowired
	private TrashDAO trashDAO;
	
	@Autowired
	private EmpDAO empDAO;

	@Override
	public int moveDirectory(int document_idx, int targetFolderIdx) {
		Document document = documentDAO.select(document_idx);
		document.setFolder(folderDAO.select(targetFolderIdx));
		return documentDAO.update(document);
	}

	@Override
	public int  createFolder(Folder folder) {
		return folderDAO.insert(folder);
	}

	@Override
	@Transactional
	public void deleteFolder(int folder_idx, int emp_idx) {
		List<Folder> childFolderList = folderDAO.selectSub(folder_idx);
		List<Document> documentList = documentDAO.selectByFolderIdx(folder_idx);
		if(documentList != null) {
			for(Document document : documentList) {
				Trash trash = new Trash();
				trash.setDocument(document);
				trash.setEmp(empDAO.selectByEmpIdx(emp_idx));
				trashDAO.insert(trash);
			}
		}
		if(childFolderList != null) {
			for(Folder folder : childFolderList) {
				deleteFolder(folder.getFolderIdx(), emp_idx);
			}
		}
		folderDAO.delete(folder_idx);
	}

	@Override
	public int updateFolder(Folder folder) {
		return folderDAO.update(folder);
	}
	
	@Override
	public Folder select(int folder_idx) {
		return folderDAO.select(folder_idx); 
	}

	@Override
	public List<Folder> selectSub(int folder_idx) {
		return folderDAO.selectSub(folder_idx);
	}

	@Override
	public List<Folder> selectAll() {
		return folderDAO.selectAll();
	}
	
	@Override
	public int selectDepth(int folder_idx) {
		int depth = 1;
		Integer parentFolder_idx = folder_idx;
		Folder folder = null;
		while(true) {
			folder = folderDAO.select(parentFolder_idx);
			Folder parentFolder = folder.getParentFolder();
			if (parentFolder == null) {
				break;
			}
			parentFolder_idx = parentFolder.getFolderIdx();
			depth++;
			
		} 
		return depth;
	}
	
	// 현재 폴더부터 최상위 폴더까지 리스트에 담은 후 순서 반전
	@Override
	public List<String> selectPath(int folder_idx) {
		Folder folder = null; 
		Integer parentFolder_idx = folder_idx;
		List<String> folderNameList = new ArrayList<String>();
		folderNameList.add(folderDAO.select(folder_idx).getFolderName());
		while(true) {
			folder = folderDAO.select(parentFolder_idx);
			Folder parentFolder = folder.getParentFolder();
			if (parentFolder == null) {
				break;
			}
			parentFolder_idx = parentFolder.getFolderIdx();
			folderNameList.add(folderDAO.select(parentFolder_idx).getFolderName());
		}
		Collections.reverse(folderNameList);
		return folderNameList;
	}
	
	public List<Folder> selectTopFolder(){
		return folderDAO.selectTopFolder();
	}
	
	public List<Folder> completeFolderList(List<Folder> folderList){
		List<Folder> folderList1 = new ArrayList<Folder>();
		List<Folder> folderList2 = new ArrayList<Folder>();
		folderList1.addAll(folderList);
		while(true) {
			for (Folder folder : folderList1) {			
				List<Folder> subList = folderDAO.selectSub(folder.getFolderIdx());				
				folder.setChildFolderList(subList);
				folderList2.addAll(subList);
			}
			folderList1.clear();
			if (folderList2.size() == 0) {
				break;
			}
			folderList1.addAll(folderList2);
			folderList2.clear();
		}
		return folderList;
	}
	
	public Folder completeFolder(int folder_idx) throws FolderException{
		int count = 0;
		Folder folder = folderDAO.select(folder_idx);
		List<Folder> folderList1 = new ArrayList<Folder>();
		List<Folder> folderList2 = new ArrayList<Folder>();
		folderList1.add(folder);
		while(true) {
			for (Folder folderDTO : folderList1) {			
				List<Folder> subList = folderDAO.selectSub(folderDTO.getFolderIdx());				
				folderDTO.setChildFolderList(subList);
				folderList2.addAll(subList);
			}
			folderList1.clear();
			if (folderList2.isEmpty()) {
				break;
			}
			folderList1.addAll(folderList2);
			folderList2.clear();
			count++;
			if (count > 1000) {
				throw new FolderException("폴더 DTO 생성 중 무한루프 발생 or 반복횟수 너무 많음");
			}
		}
		return folder;
	}
	
	
	public Folder fillSub(Folder folder) {
		List<Folder> subList = folderDAO.selectSub(folder.getFolderIdx());
		folder.setChildFolderList(subList);
		return folder;
	}
	
	@Override
	public Folder fillDocument(int folder_idx) {
		Folder folder = folderDAO.select(folder_idx);
		List<Document> documentList = documentDAO.selectByFolderIdx(folder_idx);
		folder.setDocumentList(documentList);
		return folder;
	}
	
	@Override
	public Folder completeFolderWithDocument(int folder_idx) throws Throwable {
		int count = 0;
		Folder folder = folderDAO.select(folder_idx);
		List<Folder> folderList1 = new ArrayList<Folder>();
		List<Folder> folderList2 = new ArrayList<Folder>();
		folderList1.add(folder);
		while(true) {
			for (Folder folderDTO : folderList1) {			
				List<Folder> subList = folderDAO.selectSub(folderDTO.getFolderIdx());				
				folderDTO.setChildFolderList(subList);
				List<Document> documentList = documentDAO.selectByFolderIdx(folderDTO.getFolderIdx());
				folderDTO.setDocumentList(documentList);
				folderList2.addAll(subList);
			}
			folderList1.clear();
			if (folderList2.isEmpty()) {
				break;
			}
			folderList1.addAll(folderList2);
			folderList2.clear();
			count++;
			if (count > 1000) {
				throw new FolderException("폴더 DTO 생성 중 무한루프 발생 or 반복횟수 너무 많음");
			}
		}
		return folder;
	}
	
}
