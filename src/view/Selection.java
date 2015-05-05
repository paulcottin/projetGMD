package view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import sun.net.www.content.image.jpeg;
import model.Search;
import model.SearchHandler;

public class Selection extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Color BACKGROUND_COLOR = Color.lightGray;
	
	private Options options;
	private Searcher searcher;
	private Progress progress;
	private SearchHandler search;
	private Sort sort;
	
	public Selection(SearchHandler s) {
		super();
		this.search = s;
		this.setBackground(Selection.BACKGROUND_COLOR);
		JPanel opt = new JPanel();
		JPanel sear = new JPanel();
		JPanel prog = new JPanel();
		JPanel sort = new JPanel();
		opt.setBackground(Selection.BACKGROUND_COLOR);
		sear.setBackground(Selection.BACKGROUND_COLOR);
		prog.setBackground(Selection.BACKGROUND_COLOR);
		sort.setBackground(Selection.BACKGROUND_COLOR);
		
		options = new Options(this.search);
		searcher = new Searcher(this.search);
		progress = new Progress(this.search);
		this.sort = new Sort(this.search);
		
		opt.add(options);
		sear.add(searcher);
		prog.add(progress);
		sort.add(this.sort);
		
		this.add(opt);
		this.add(sear);

		this.add(sort);
	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

	public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

}
